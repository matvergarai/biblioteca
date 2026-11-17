import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { Sesion } from '../servicios/sesion';

export const sesionGuard: CanActivateFn = () => {
  const sesion = inject(Sesion);
  if (sesion.actual()) {
    return true;
  }
  return inject(Router).createUrlTree(['/ingresar']);
};

export const adminGuard: CanActivateFn = () => {
  const sesion = inject(Sesion);
  if (sesion.esAdministrador()) {
    return true;
  }
  return inject(Router).createUrlTree(['/']);
};
