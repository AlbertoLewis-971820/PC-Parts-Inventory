import { Routes } from '@angular/router';
import { Login } from './component/auth/login/login';
import { Inventory } from './component/inventory/inventory';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login},
  { path: '', component: Inventory, canActivate: [authGuard]  }
];
