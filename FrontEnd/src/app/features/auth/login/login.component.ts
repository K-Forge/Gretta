import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest } from '../../../core/models';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      correo: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const credentials: LoginRequest = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.loading = false;
        // Redirigir según el rol
        const user = this.authService.getCurrentUser();
        if (user?.rol === 'ESTILISTA') {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/citas']);
        }
      },
      error: (error) => {
        this.loading = false;
        console.error('Error de login completo:', error);
        
        // Intentar obtener el mensaje de error de diferentes estructuras posibles
        const backendMessage = error.error?.mensaje || error.error?.message || error.error?.detalle;
        
        this.errorMessage = backendMessage || 'Credenciales inválidas. Por favor, intenta de nuevo.';
      }
    });
  }

  get correo() {
    return this.loginForm.get('correo');
  }

  get contrasena() {
    return this.loginForm.get('contrasena');
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
}
