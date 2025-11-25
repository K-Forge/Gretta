import { Injectable, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap, BehaviorSubject } from 'rxjs';
import { ApiService } from './api.service';
import { 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  Usuario, 
  RolUsuario 
} from '../models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'authToken';
  private readonly USER_KEY = 'currentUser';
  private readonly CLIENT_ID_KEY = 'clientId';
  private readonly STYLIST_ID_KEY = 'stylistId';

  // Señales para manejo reactivo del estado
  private currentUserSubject = new BehaviorSubject<Usuario | null>(this.getUserFromStorage());
  public currentUser$ = this.currentUserSubject.asObservable();

  // Computed signals
  isAuthenticated = computed(() => !!this.getCurrentUser());
  currentUserRole = computed(() => this.getCurrentUser()?.rol || null);
  isCliente = computed(() => this.getCurrentUser()?.rol === RolUsuario.CLIENTE);
  isEstilista = computed(() => this.getCurrentUser()?.rol === RolUsuario.ESTILISTA);

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {}

  /**
   * Iniciar sesión
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.apiService.post<AuthResponse>('auth/login', credentials).pipe(
      tap(response => this.handleAuthentication(response))
    );
  }

  /**
   * Registrar nuevo usuario
   * El backend devuelve solo un mensaje, luego hacemos login automático
   */
  register(userData: RegisterRequest): Observable<any> {
    console.log('📤 Enviando registro al backend:', userData);
    return this.apiService.post<{mensaje: string}>('auth/register', userData).pipe(
      tap(response => console.log('✅ Respuesta del registro:', response)),
      tap({
        error: (error) => console.error('❌ Error en registro:', error)
      })
    );
  }

  /**
   * Registrar y hacer login automático
   */
  registerAndLogin(userData: RegisterRequest): Observable<AuthResponse> {
    return new Observable(observer => {
      console.log('🔄 Iniciando proceso de registro y login automático...');
      this.register(userData).subscribe({
        next: (response) => {
          console.log('✅ Registro exitoso, procediendo con login automático...');
          // Después de registrar, hacer login automático
          const loginData: LoginRequest = {
            correo: userData.correo,
            contrasena: userData.contrasena
          };
          this.login(loginData).subscribe({
            next: (authResponse) => {
              console.log('✅ Login automático exitoso');
              observer.next(authResponse);
              observer.complete();
            },
            error: (error) => {
              console.error('❌ Error en login automático:', error);
              observer.error({ ...error, message: 'Registro exitoso pero falló el login automático. Intenta iniciar sesión manualmente.' });
            }
          });
        },
        error: (error) => {
          console.error('❌ Error en el registro:', error);
          observer.error(error);
        }
      });
    });
  }

  /**
   * Cerrar sesión
   */
  logout(): void {
    this.clearAuthData();
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Obtener usuario actual
   */
  getCurrentUser(): Usuario | null {
    return this.currentUserSubject.value;
  }

  /**
   * Obtener token JWT
   */
  getToken(): string | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    if (!token || token === 'null' || token === 'undefined') {
      return null;
    }
    return token;
  }

  /**
   * Verificar si el usuario tiene un rol específico
   */
  hasRole(role: RolUsuario): boolean {
    const user = this.getCurrentUser();
    return user?.rol === role;
  }

  /**
   * Verificar si el usuario está autenticado
   */
  isLoggedIn(): boolean {
    return !!this.getToken() && !!this.getCurrentUser();
  }

  /**
   * Verificar si el token está expirado
   */
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expiry = payload.exp * 1000; // Convertir a milisegundos
      return Date.now() > expiry;
    } catch (error) {
      return true;
    }
  }

  /**
   * Renovar token (si el backend lo soporta)
   */
  refreshToken(): Observable<AuthResponse> {
    return this.apiService.post<AuthResponse>('auth/refresh', {}).pipe(
      tap(response => {
        this.setToken(response.token);
      })
    );
  }

  /**
   * Manejar la autenticación exitosa
   */
  private handleAuthentication(response: AuthResponse): void {
    this.setToken(response.token);
    this.setUser(response.usuario);
    if (response.idCliente) {
      localStorage.setItem(this.CLIENT_ID_KEY, response.idCliente.toString());
    }
    if (response.idEstilista) {
      localStorage.setItem(this.STYLIST_ID_KEY, response.idEstilista.toString());
    }
    this.currentUserSubject.next(response.usuario);
  }

  /**
   * Guardar token en localStorage
   */
  private setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /**
   * Guardar usuario en localStorage
   */
  private setUser(user: Usuario): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  /**
   * Obtener usuario desde localStorage
   */
  private getUserFromStorage(): Usuario | null {
    const userJson = localStorage.getItem(this.USER_KEY);
    if (userJson && userJson !== 'null' && userJson !== 'undefined') {
      try {
        return JSON.parse(userJson);
      } catch (error) {
        return null;
      }
    }
    return null;
  }

  /**
   * Limpiar datos de autenticación
   */
  private clearAuthData(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.CLIENT_ID_KEY);
    localStorage.removeItem(this.STYLIST_ID_KEY);
  }

  /**
   * Actualizar datos del usuario actual (después de editar perfil)
   */
  updateCurrentUser(user: Usuario): void {
    this.setUser(user);
    this.currentUserSubject.next(user);
  }

  /**
   * Obtener ID del cliente actual
   */
  getClientId(): number | null {
    const id = localStorage.getItem(this.CLIENT_ID_KEY);
    return id ? parseInt(id, 10) : null;
  }

  /**
   * Obtener ID del estilista actual
   */
  getStylistId(): number | null {
    const id = localStorage.getItem(this.STYLIST_ID_KEY);
    return id ? parseInt(id, 10) : null;
  }
}
