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

  manufacturerSearch: string = '';

  categorySearch: string = '';

  lowStockThreshold: number | null | string = null;

  restockPartId: number | null = null;
  restockQuantity: number | null = null;



  constructor(private pcPartService: PcPartService) {

  }
startEditing(part: PcPart): void {
  this.editingPart = { ...part }; // Create a copy of the part to edit
}


startRestocking(part: PcPart): void{
  if(part.id === undefined){
    console.error('Invalid part ID');
    return;

    }

  this.restockPartId = part.id;
  this.restockQuantity = null;

}

cancelRestock(): void{
  this.restockQuantity = null;
  this.restockPartId = null;
  }

confirmRestock(): void{
  if(this.restockPartId === null || this.restockQuantity === null || this.restockQuantity <= 0){
      console.log('Invalid restock quantity');
      return;
    }
  this.pcPartService.restockPcPart(
      this.restockPartId,
      this.restockQuantity
  ).subscribe({
      next: (updatedPart: PcPart) => {
        const currentParts = this.pcParts();

        this.pcParts.set(
          currentParts.map(part =>
            part.id === updatedPart.id ? updatedPart : part
            )
          );
        this.restockPartId = null;
            this.restockQuantity = null;
      },
      error: (error) => {
          console.error('Error restocking PC part:', error);
      }

  });
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

searchByCategory(): void {
  if(this.categorySearch.trim() === ''){
      this.pcPartService.getAllPcParts().subscribe({
          next: (data: PcPart[]) => {
              console.log('Got all the pc parts', data);
              this.pcParts.set(data);
            },
          error: (error) => {
              console.log('Error getting pc parts:',error);
            }
        });
      return;
    }
  this.pcPartService.getPcPartsByCategory(this.categorySearch).subscribe({
          next: (data: PcPart[]) => {
                   console.log('Got parts for category: ', data);
                   this.pcParts.set(data);
                 },
               error: (error) => {
                   console.error('Error getting PC parts:', error);
                 }

    })
}


searchByManufacturer(): void{
  if(this.manufacturerSearch.trim() === ''){
      this.pcPartService.getAllPcParts().subscribe({
        next: (data: PcPart[]) => {
          console.log('Got all the pc parts!', data);
          this.pcParts.set(data);
          },
        error: (error) => {
          console.error('Error getting pc parts:',error);
          }

        });

      return;

    }

  this.pcPartService.getPcPartsByManufacturer(this.manufacturerSearch).subscribe({
         next: (data: PcPart[]) => {
             console.log('Got parts for manufacturer: ', data);
             this.pcParts.set(data);
           },
         error: (error) => {
             console.error('Error getting PC parts:', error);
           }
    });
}

searchLowStock(): void {
  if(this.lowStockThreshold === null || this.lowStockThreshold === '') {
    this.pcPartService.getAllPcParts().subscribe({
      next: (data: PcPart[]) => {
        console.log('Got all the pc parts!', data);
        this.pcParts.set(data);
      },
      error: (error) => {
        console.error('Error getting pc parts:', error);
      }
    });

    return;
  }

  const threshold = Number(this.lowStockThreshold);

  this.pcPartService.getLowStockPcParts(threshold).subscribe({
    next: (data: PcPart[]) => {
      console.log('Got low stock parts: ', data);
      this.pcParts.set(data);
    },
    error: (error) => {
      console.error('Error getting PC parts:', error);
    }
  });
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
