import { Component } from '@angular/core';
import { QuizzService } from '../../_services/quizz.service';
import { GenAI } from '../../../../types';
import { AuthService } from '../../_services/auth.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

const quizzApi = 'http://localhost:8081/quizz/';

@Component({
    selector: 'app-add-with-ai',
    standalone: false,
    templateUrl: './add-with-ai.component.html',
    styleUrl: './add-with-ai.component.css'
})
export class AddWithAIComponent {
    constructor(private quizzService: QuizzService, private authService: AuthService, private toastr: ToastrService,
        private router: Router
    ) { }

    data: GenAI = {
        topic: '',
        numQuestions: 0,
        userId: Number(this.authService.getTokenData()?.userId),
        quizzName: ''
    }

    add() {
        this.quizzService.addQuizz(quizzApi + "generate-ai", this.data).subscribe(() => {
            this.toastr.success("Add new quiz successfully")

            this.router.navigate(['']);
        });
    }
}
