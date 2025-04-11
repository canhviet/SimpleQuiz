import { Component } from '@angular/core';
import { QuizzService } from '../../_services/quizz.service';
import { DataResponse, QuizResponse } from '../../../../types';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../_services/auth.service';

@Component({
    selector: 'app-your-quizz',
    standalone: false,
    templateUrl: './your-quizz.component.html',
    styleUrl: './your-quizz.component.css'
})
export class YourQuizzComponent {
    constructor(private quizzService: QuizzService,
        private toastr: ToastrService,
        private authService: AuthService) { }

    quizzs: QuizResponse[] = [];

    ngOnInit() {
        this.quizzService.quizOfUser(Number(this.authService.getTokenData()?.userId))
            .subscribe((data: DataResponse) => { this.quizzs = data.data });

    }

    copy(quizzId: number | undefined) {
        if (quizzId) {
            const text = "http://localhost:4200/quiz/" + quizzId;
            navigator.clipboard.writeText(text).then(() => {
                this.toastr.info("copied to clipboard!")
            });
        }
    }

}
