import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { DataResponse } from '../../../types';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  constructor(private apiService: ApiService) { }

  private baseUrl = 'http://localhost:8081/history/';

  getAll = (userId: number): Observable<DataResponse> => {
    return this.apiService.get(`${this.baseUrl}${userId}`, { responseType: 'json' });
  };
}
