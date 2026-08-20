import { PcPart } from '../../models/pc-part';
import { PcPartService } from '../../services/pc-part';
import { Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';


@Component({
  selector: 'app-pc-part-list',
  imports: [],
  templateUrl: './pc-part-list.html',
  styleUrl: './pc-part-list.css',
})
export class PcPartList implements OnInit {
  pcParts = signal<PcPart[]>([]);

  constructor(private pcPartService: PcPartService) {

  }

  ngOnInit(): void {
    this.pcPartService.getAllPcParts().subscribe(
      (data: PcPart[]) => {
        console.log('Fetched PC parts:', data);
        this.pcParts.set(data);
      },
      (error) => {
        console.error('Error fetching PC parts:', error);
      }
    );
  }
}
