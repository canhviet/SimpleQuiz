import { Component } from '@angular/core';
import { HistoryRequest, JwtPayload, Quizz } from '../../../../types';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { QuizzService } from '../../_services/quizz.service';
import { ToastrService } from 'ngx-toastr';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import Swal from 'sweetalert2';
import { AuthService } from '../../_services/auth.service';
import { jwtDecode } from 'jwt-decode';

@Component({
    selector: 'app-quiz',
    standalone: false,
    templateUrl: './quiz.component.html',
    styleUrl: './quiz.component.css'
})
export class QuizComponent {
    constructor(private route: ActivatedRoute, private quizService: QuizzService,
        private toastr: ToastrService, private router: Router, private sanitizer: DomSanitizer,
        private authService: AuthService
    ) { }

    quiz: Quizz = {
        id: 0,
        userId: 0,
        name: '',
        questions: []
    }

    history: HistoryRequest = {
        score: '',
        userId: 0,
        quizzId: 0
    }

    answers: number[] = [];
    score: number = 0;
    username: string = "";

    ngOnInit() {
        const token = this.authService.getTokenData();

        if (token) {
            const decoded = jwtDecode<JwtPayload>(token.accessToken);
            this.username = decoded.username;
            this.toastr.success("Hello " + this.username);
        } else {
            this.toastr.error("please login!")
            this.router.navigate(['']);
        }

        this.route.params.subscribe((params) => { this.quiz.id = params['quizId']; })
        this.quizService.getQuizzById(Number(this.quiz.id)).subscribe(
            {
                next: (res) => {
                    this.quiz = res.data;
                    this.history.quizzId = Number(this.quiz.id);
                    this.history.userId = Number(this.quiz.userId);
                    this.answers = new Array(this.quiz.questions.length);
                }
            });
    }

    submit() {
        this.score = 0;
        this.quiz.questions.forEach((question, index) => {
            const selectedOptionIndex = this.answers[index];
            const selectedOption = question.options[selectedOptionIndex];

            if (selectedOption && selectedOption.isCorrect) {
                this.score++;
            }
        })

        this.toastr.info("Your's Score: " + this.score);

        this.history.score = this.score + "/" + this.answers.length;

        this.quizService.addQuizz('http://localhost:8081/history/', this.history).subscribe((res) => { console.log(res) });

        this.router.navigate(['home']);

    }

    getSafeHtml(html: string): SafeHtml {
        return this.sanitizer.bypassSecurityTrustHtml(html);
    }

    confirmSumit() {
        Swal.fire({
            title: 'Are you sure?',
            text: 'You will not be able to submit again!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Yes, do it!',
            cancelButtonText: 'No, cancel!',
        }).then(() => {
            this.submit();
        });
    }

}
