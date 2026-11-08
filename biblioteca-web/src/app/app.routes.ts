import { Routes } from '@angular/router';

import { DetalleLibroComponent } from './paginas/detalle-libro/detalle-libro.component';
import { FormularioLibroComponent } from './paginas/formulario-libro/formulario-libro.component';
import { ListaLibrosComponent } from './paginas/lista-libros/lista-libros.component';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'libros' },
  { path: 'libros', component: ListaLibrosComponent },
  { path: 'libros/nuevo', component: FormularioLibroComponent },
  { path: 'libros/:id', component: DetalleLibroComponent },
  { path: 'libros/:id/editar', component: FormularioLibroComponent },
];
