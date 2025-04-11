import { Component } from '@angular/core';
import { QuizzService } from '../../_services/quizz.service';
import { Question, Quizz, TokenResponse } from '../../../../types';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';
import { AuthService } from '../../_services/auth.service';
import { Router } from '@angular/router';

const quizzApi = 'http://localhost:8081/quizz/';

@Component({
    selector: 'app-add-quizz',
    templateUrl: './add-quizz.component.html',
    styleUrls: ['./add-quizz.component.css'],
    standalone: false,
})
export class AddQuizzComponent {
    constructor(
        private quizzService: QuizzService,
        private toastr: ToastrService,
        private authService: AuthService,
        private router: Router
    ) { }

    quizz: Quizz = {
        name: '',
        userId: Number(this.authService.getTokenData()?.userId),
        questions: []
    };

    ngOnInit() { }

    addQuestion() {
        let newQuestion: Question = {
            title: '',
            options: [],
        };

        for (let i = 0; i < 4; i++) {
            newQuestion.options.push({
                title: '',
                isCorrect: false,
            });
        }

        this.quizz.questions.push(newQuestion);
    }

    submit() {

        this.quizz.questions.forEach((question) => {
            this.ensureFirstOptionSelected(question);
        });

        this.quizzService.addQuizz(quizzApi, this.quizz).subscribe(() => {
            this.toastr.success("Add new quiz successfully")

            this.router.navigate(['']);
        });

    }

    setCorrectAnswer(question: Question, optionIndex: number) {
        question.options.forEach((o) => (o.isCorrect = false));

        question.options[optionIndex].isCorrect = true;
    }

    ensureFirstOptionSelected(question: Question) {
        const hasCorrectOption = question.options.some((o) => o.isCorrect);

        if (!hasCorrectOption && question.options.length > 0) {
            question.options[0].isCorrect = true;
        }
    }

    QuillConfiguration = {
        toolbar: [
            ['bold', 'italic', 'underline'],
            [{ color: [] }, { background: [] }],
            ['link', 'image', 'video'],
            ['clean'],
        ],
    };

    confirmDelete(index: number) {
        Swal.fire({
            title: 'Are you sure?',
            text: 'You will not be able to recover this question!',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'No, cancel!',
        }).then((result) => {
            if (result.isConfirmed) {
                this.removeQuestion(index);
                this.toastr.success(
                    'Question deleted successfully!',
                    'Deleted'
                );
            }
        });
    }

    removeQuestion(index: number) {
        this.quizz.questions.splice(index, 1);
    }

    hasContent(content: string): boolean {
        return content !== null && content !== undefined && content.trim() !== '' && content !== '<p><br></p>';
    }
}
