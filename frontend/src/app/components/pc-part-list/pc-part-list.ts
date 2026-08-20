import { PcPart } from '../../models/pc-part';
import { PcPartService } from '../../services/pc-part-service';
import { Component, OnInit } from '@angular/core';
import { signal } from '@angular/core';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-pc-part-list',
  imports: [FormsModule],
  templateUrl: './pc-part-list.html',
  styleUrl: './pc-part-list.css',
})
export class PcPartList implements OnInit {
  pcParts = signal<PcPart[]>([]);

  editingPart: PcPart | null = null;



  constructor(private pcPartService: PcPartService) {

  }
startEditing(part: PcPart): void {
  this.editingPart = { ...part }; // Create a copy of the part to edit
}

cancelEditing(): void {
  this.editingPart = null; // Cancel editing
}

saveEditing(): void {
  if (this.editingPart && this.editingPart.id !== undefined) {
    this.pcPartService.updatePcPart(this.editingPart.id, this.editingPart).subscribe({
      next: (updatedPart: PcPart) => {
        console.log('Updated part:', updatedPart);
        const currentParts = this.pcParts();

        this.pcParts.set(
            currentParts.map(part => part.id === updatedPart.id ? updatedPart : part));

        this.editingPart = null; // Clear editing state
      },
      error: (error) => {
        console.error('Error updating part:', error);
      }
    });
  }
}

addPart(part: PcPart): void {
  const currentParts = this.pcParts();
  this.pcParts.set([...currentParts, part]);
}

deletePcPart(id: number | undefined): void {
  if (id === undefined) {
    console.error('Invalid part ID');
    return;
  }

  this.pcPartService.deletePcPart(id).subscribe({
    next: () => {
      console.log('Part deleted:', id);

      const currentParts = this.pcParts();
      this.pcParts.set(
        currentParts.filter(part => part.id !== id)
      );
    },
    error: (error) => {
      console.error('Error deleting part:', error);
    }
  });
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
