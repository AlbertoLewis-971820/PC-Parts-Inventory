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

}
