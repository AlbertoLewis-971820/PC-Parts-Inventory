import { Component } from '@angular/core';
import { PcPartForm } from '../pc-part-form/pc-part-form';
import { PcPartList } from '../pc-part-list/pc-part-list';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-inventory',
  imports: [PcPartForm, PcPartList],
  templateUrl: './inventory.html',
  styleUrl: './inventory.css',
})
export class Inventory {


  constructor(private authService: AuthService, private router: Router) { }


  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        console.log('Logout successful');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.error('Logout failed:', error);
      }
    });
  }


}
