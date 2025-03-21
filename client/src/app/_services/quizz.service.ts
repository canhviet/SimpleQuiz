import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
export class QuizzService {
    constructor(private apiService: ApiService) {}

    addQuizz = (url: string, body: any): Observable<any> => {
        return this.apiService.post(url, body, {});
    };
}
