import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RegisterRequest, RolUsuario, TipoDocumento, CanalComunicacion } from '../../../core/models';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';
  hidePassword = true;

  // Opciones para los selects
  roles = [
    { value: RolUsuario.CLIENTE, label: 'Cliente' },
    { value: RolUsuario.ESTILISTA, label: 'Estilista' }
  ];

  tiposDocumento = [
    { value: TipoDocumento.CEDULA, label: 'Cédula' },
    { value: TipoDocumento.PASAPORTE, label: 'Pasaporte' },
    { value: TipoDocumento.NIT, label: 'NIT' },
    { value: TipoDocumento.CEDULA_EXTRANJERIA, label: 'Cédula Extranjería' }
  ];

  canalesPreferidos = [
    { value: CanalComunicacion.EMAIL, label: 'Email' },
    { value: CanalComunicacion.WHATSAPP, label: 'WhatsApp' },
    { value: CanalComunicacion.SMS, label: 'SMS' }
  ];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      correo: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9+\-\s()]+$/)]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]],
      tipoDocumento: [TipoDocumento.CEDULA, Validators.required],
      numeroDocumento: ['', Validators.required],
      rol: [RolUsuario.CLIENTE, Validators.required],
      canalPreferido: [CanalComunicacion.EMAIL],
      // Campos para estilista
      especialidad: [''],
      disponibilidad: ['']
    });

    // Escuchar cambios en el rol para validar campos de estilista
    this.registerForm.get('rol')?.valueChanges.subscribe((rol) => {
      if (rol === RolUsuario.ESTILISTA) {
        this.registerForm.get('especialidad')?.setValidators([Validators.required]);
        this.registerForm.get('disponibilidad')?.setValidators([Validators.required]);
      } else {
        this.registerForm.get('especialidad')?.clearValidators();
        this.registerForm.get('disponibilidad')?.clearValidators();
      }
      this.registerForm.get('especialidad')?.updateValueAndValidity();
      this.registerForm.get('disponibilidad')?.updateValueAndValidity();
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.errorMessage = '❌ Por favor completa todos los campos requeridos correctamente.';
      console.error('Formulario inválido:', this.registerForm.errors);
      Object.keys(this.registerForm.controls).forEach(key => {
        const control = this.registerForm.get(key);
        if (control?.invalid) {
          console.error(`Campo ${key} inválido:`, control.errors);
        }
      });
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const userData: RegisterRequest = this.registerForm.value;
    console.log('Datos a enviar:', userData);

    this.authService.registerAndLogin(userData).subscribe({
      next: (response) => {
        this.loading = false;
        this.successMessage = '🧜‍♀️ ¡Registro exitoso! Bienvenida al mundo marino de Gretta...';
        setTimeout(() => {
          const user = this.authService.getCurrentUser();
          if (user?.rol === RolUsuario.ESTILISTA) {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/citas']);
          }
        }, 1500);
      },
      error: (error: any) => {
        this.loading = false;
        console.error('Error completo de registro:', error);
        console.error('Error.error:', error.error);
        console.error('Error.message:', error.message);
        console.error('Error.status:', error.status);
        
        // Manejo detallado de errores
        if (error.status === 0) {
          this.errorMessage = '❌ No se puede conectar con el servidor. Verifica que el backend esté ejecutándose.';
        } else if (error.error?.detalle) {
          this.errorMessage = `❌ ${error.error.detalle}`;
        } else if (error.error?.mensaje) {
          this.errorMessage = `❌ ${error.error.mensaje}`;
        } else if (error.message) {
          this.errorMessage = `❌ ${error.message}`;
        } else {
          this.errorMessage = '❌ Error al registrar. Por favor, intenta de nuevo.';
        }
      }
    });
  }

  get isEstilista(): boolean {
    return this.registerForm.get('rol')?.value === RolUsuario.ESTILISTA;
  }

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
}
