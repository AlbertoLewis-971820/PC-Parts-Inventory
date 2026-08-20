import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PcPart } from './models/pc-part';
import { PcPartList } from './components/pc-part-list/pc-part-list';
import { PcPartForm } from './component/pc-part-form/pc-part-form';

@Component({
  selector: 'app-root',
  imports: [PcPartList, PcPartForm],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  onPartAdded(part: PcPart): void{
    console.log('Part added:', part);}
}
