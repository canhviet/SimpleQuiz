import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { DataResponse } from '../../../types';

@Injectable({
    providedIn: 'root',
})
export class QuizzService {
    constructor(private apiService: ApiService) { }
    private baseUrl = 'http://localhost:8081/quizz';

    addQuizz = (url: string, body: any): Observable<any> => {
        return this.apiService.post(url, body, {});
    };

    getQuizzById = (quizzId: number): Observable<DataResponse> => {
        return this.apiService.get(`${this.baseUrl}/${quizzId}`, { responseType: 'json' });
    };

    quizOfUser = (userId: number): Observable<DataResponse> => {
        return this.apiService.get(`${this.baseUrl}/user/${userId}`, { responseType: 'json' });
    };

    updateQuizz = (quizzId: number, body: any): Observable<any> => {
        return this.apiService.put(`${this.baseUrl}/${quizzId}`, body, {});
    };

    deleteQuizz = (quizzId: number): Observable<any> => {
        return this.apiService.delete(`${this.baseUrl}/${quizzId}`, {});
    };

    searchQuizz = (keyword: string): Observable<DataResponse> => {
        return this.apiService.get(`${this.baseUrl}/search?keyword=${keyword}`, { responseType: 'json' });
    };
}
