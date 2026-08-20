import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PcPart } from '../models/pc-part';

@Injectable({
  providedIn: 'root'
})
export class PcPartService {

  private apiUrl = 'http://localhost:8080/api/pcparts';
  constructor(private httpClient: HttpClient) { }
  // Get all PC parts from the backend API
  getAllPcParts(): Observable<PcPart[]> {
    return this.httpClient.get<PcPart[]>(this.apiUrl);
  }

  //Add a new PC part to the backend API
  addPcPart(pcPart: PcPart): Observable<PcPart> {
    return this.httpClient.post<PcPart>(this.apiUrl, pcPart);
  }

  deletePcPart(id: number): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.httpClient.delete<void>(url);
  }

  updatePcPart(id: number, pcPart: PcPart): Observable<PcPart> {
    const url = `${this.apiUrl}/${id}`;
    return this.httpClient.put<PcPart>(url, pcPart);
  }

}
