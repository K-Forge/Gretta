import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { Usuario, ActualizarPerfilRequest, CambiarContrasenaRequest } from '../../core/models';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  currentUser: Usuario | null = null;
  perfilForm: FormGroup;
  passwordForm: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';
  activeTab: 'perfil' | 'password' = 'perfil';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private apiService: ApiService
  ) {
    this.perfilForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      telefono: ['', Validators.required],
      canalPreferido: ['']
    });

    this.passwordForm = this.fb.group({
      contrasenaActual: ['', Validators.required],
      nuevaContrasena: ['', [Validators.required, Validators.minLength(6)]],
      confirmarContrasena: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    if (this.currentUser) {
      this.perfilForm.patchValue({
        nombre: this.currentUser.nombre,
        apellido: this.currentUser.apellido,
        telefono: this.currentUser.telefono,
        canalPreferido: this.currentUser.canalPreferido
      });
    }
  }

  onSubmitPerfil(): void {
    if (this.perfilForm.invalid) return;

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request: ActualizarPerfilRequest = this.perfilForm.value;

    this.apiService.put<Usuario>('usuarios/perfil', request).subscribe({
      next: (usuario) => {
        this.authService.updateCurrentUser(usuario);
        this.successMessage = 'Perfil actualizado exitosamente';
        this.loading = false;
      },
      error: (error: any) => {
        this.errorMessage = 'Error al actualizar perfil';
        this.loading = false;
      }
    });
  }

  onSubmitPassword(): void {
    if (this.passwordForm.invalid) return;

    const { nuevaContrasena, confirmarContrasena } = this.passwordForm.value;
    if (nuevaContrasena !== confirmarContrasena) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request: CambiarContrasenaRequest = {
      contrasenaActual: this.passwordForm.value.contrasenaActual,
      nuevaContrasena: this.passwordForm.value.nuevaContrasena
    };

    this.apiService.put('usuarios/cambiar-contrasena', request).subscribe({
      next: () => {
        this.successMessage = 'Contraseña cambiada exitosamente';
        this.passwordForm.reset();
        this.loading = false;
      },
      error: (error: any) => {
        this.errorMessage = 'Error al cambiar contraseña';
        this.loading = false;
      }
    });
  }

  setActiveTab(tab: 'perfil' | 'password'): void {
    this.activeTab = tab;
    this.successMessage = '';
    this.errorMessage = '';
  }
}
