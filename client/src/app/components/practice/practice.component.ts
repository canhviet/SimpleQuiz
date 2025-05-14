import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { GenAI } from '../../../../types';
import { AuthService } from '../../_services/auth.service';
import { QuizzService } from '../../_services/quizz.service';

const quizzApi = 'http://localhost:8081/quizz/';

@Component({
  selector: 'app-practice',
  standalone: false,
  templateUrl: './practice.component.html',
  styleUrl: './practice.component.css'
})
export class PracticeComponent {
    constructor(private quizzService: QuizzService, private authService: AuthService,
        private router: Router
    ) { }

    generated: boolean = false;

    data: GenAI = {
        topic: '',
        numQuestions: 0,
        userId: Number(this.authService.getTokenData()?.userId),
        quizzName: ''
    }

    add() {
        this.data.quizzName = "Practice " + this.data.topic;

        this.quizzService.addQuizz(quizzApi + "generate-ai", this.data).subscribe((res) => {
            this.router.navigate(['quiz', res.data]);
        });
    }
}
