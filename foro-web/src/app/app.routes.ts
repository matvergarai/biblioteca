import { Routes } from '@angular/router';

import { adminGuard, sesionGuard } from './guardias/acceso';
import { Ingresar } from './paginas/ingresar/ingresar';
import { Inicio } from './paginas/inicio/inicio';
import { Moderacion } from './paginas/moderacion/moderacion';
import { Perfil } from './paginas/perfil/perfil';
import { Publicar } from './paginas/publicar/publicar';
import { Recuperar } from './paginas/recuperar/recuperar';
import { Registro } from './paginas/registro/registro';
import { TemaDetalle } from './paginas/tema/tema';
import { Temas } from './paginas/temas/temas';

export const routes: Routes = [
  { path: '', component: Inicio },
  { path: 'ingresar', component: Ingresar },
  { path: 'registro', component: Registro },
  { path: 'recuperar', component: Recuperar },
  { path: 'perfil', component: Perfil, canActivate: [sesionGuard] },
  { path: 'temas', component: Temas },
  { path: 'temas/:id', component: TemaDetalle },
  { path: 'publicar', component: Publicar, canActivate: [sesionGuard] },
  { path: 'moderacion', component: Moderacion, canActivate: [sesionGuard, adminGuard] },
  { path: '**', redirectTo: '' },
];
