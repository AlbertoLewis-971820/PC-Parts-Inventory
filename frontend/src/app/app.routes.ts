import { Routes } from '@angular/router';
import { Login } from './component/auth/login/login';
import { Register } from './component/auth/register/register';
import { Inventory } from './component/inventory/inventory';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: Login},
  { path: 'register', component: Register },
  { path: '', component: Inventory, canActivate: [authGuard]  }
];
