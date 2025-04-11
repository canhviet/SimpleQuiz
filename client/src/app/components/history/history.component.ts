import { Component } from '@angular/core';
import { HistoryService } from '../../_services/history.service';
import { DataResponse, HistoryResponse, TokenResponse } from '../../../../types';
import { QuizzService } from '../../_services/quizz.service';
import { AuthService } from '../../_services/auth.service';

@Component({
    selector: 'app-history',
    standalone: false,
    templateUrl: './history.component.html',
    styleUrl: './history.component.css'
})
export class HistoryComponent {
    constructor(private historyService: HistoryService, private quizzService: QuizzService, private authService: AuthService) { }

    list: (HistoryResponse & { quizzName?: string })[] = [];

    ngOnInit() {
        this.historyService.getAll(Number(this.authService.getTokenData()?.userId)).subscribe(
            (res: DataResponse) => {
                this.list = res.data;
                    this.list.forEach(historyItem => {
                    this.quizzService.getQuizzById(historyItem.quizzId).subscribe((res: DataResponse) => {
                        historyItem.quizzName = res.data.name;
                    });
                });
            }

        );
    }
}
