import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function claveSegura(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = String(control.value ?? '');
    if (!valor) {
      return null;
    }
    const errores: ValidationErrors = {};
    if (valor.length < 8) {
      errores['minimo'] = true;
    }
    if (valor.length > 72) {
      errores['maximo'] = true;
    }
    if (!/[A-Za-z]/.test(valor)) {
      errores['letras'] = true;
    }
    if (!/\d/.test(valor)) {
      errores['numeros'] = true;
    }
    if (!/[^A-Za-z0-9]/.test(valor)) {
      errores['especial'] = true;
    }
    return Object.keys(errores).length ? errores : null;
  };
}

export function clavesIguales(origen: string, confirmacion: string): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const clave = control.get(origen)?.value;
    const repetida = control.get(confirmacion)?.value;
    if (!repetida || clave === repetida) {
      return null;
    }
    return { distintas: true };
  };
}
