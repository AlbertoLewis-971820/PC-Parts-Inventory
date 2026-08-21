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

@Output()
partAdded = new EventEmitter<PcPart>();

  constructor(private pcPartService: PcPartService) { }
addPcPart(partForm: NgForm): void {
  this.pcPartService.addPcPart(this.newPcPart).subscribe({
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
      this.errorMessage = 'PC part added successfully!';
    },
    error: (error) => {
      this.errorMessage = 'Error adding new PC part. Please try again.';
    }
  });
}

}
