import { Component } from '@angular/core';
import { PcPart } from '../../models/pc-part';
import { FormsModule } from '@angular/forms';

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


}
