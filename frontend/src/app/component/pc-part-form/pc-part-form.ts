
import { Component, Output, EventEmitter } from '@angular/core';
import { PcPart } from '../../models/pc-part';
import { FormsModule, NgForm } from '@angular/forms';
import { PcPartService } from '../../services/pc-part-service';

@Component({
  selector: 'app-pc-part-form',
  imports: [FormsModule],
  templateUrl: './pc-part-form.html',
  styleUrl: './pc-part-form.css',
})
export class PcPartForm {

  newPcPart: PcPart = {
    name: '',
    category: '',
    manufacturer: '',
    price: 0,
    quantity: 0
  };

  errorMessage: string = '';
  successMessage: string = '';
  isSubmitting: boolean = false;

  @Output()
  partAdded = new EventEmitter<PcPart>();

  constructor(private pcPartService: PcPartService) {}

  addPcPart(partForm: NgForm): void {

    if (this.isSubmitting) {
      return;
    }

    this.errorMessage = '';
    this.successMessage = '';

    const name = this.newPcPart.name.trim();
    const manufacturer = this.newPcPart.manufacturer.trim();

    if (
      partForm.invalid ||
      !name ||
      !manufacturer ||
      !this.newPcPart.category ||
      !Number.isFinite(this.newPcPart.price) ||
      this.newPcPart.price <= 0 ||
      !Number.isInteger(this.newPcPart.quantity) ||
      this.newPcPart.quantity < 0
    ) {
      this.errorMessage = 'Please correct the highlighted fields.';
      return;
    }

    this.isSubmitting = true;

    const partToSave: PcPart = {
      ...this.newPcPart,
      name,
      manufacturer
    };

    this.pcPartService.addPcPart(partToSave).subscribe({
      next: (data: PcPart) => {
        console.log('Added new PC part:', data);

        this.partAdded.emit(data);

        this.newPcPart = {
          name: '',
          category: '',
          manufacturer: '',
          price: 0,
          quantity: 0
        };

        partForm.resetForm(this.newPcPart);

        this.successMessage = 'PC part added successfully!';
        this.isSubmitting = false;
      },
      error: (error) => {
        console.error('Error adding PC part:', error);

        this.errorMessage =
          'Unable to add PC part. Please try again.';

        this.isSubmitting = false;
      }
    });
  }
}
