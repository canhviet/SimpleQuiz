import { Component } from '@angular/core';
import { Question, Quizz } from '../../../../../types';
import { QuizzService } from '../../../_services/quizz.service';

const quizzApi = 'http://localhost:8081/quizz/';

@Component({
    selector: 'app-add-quizz',
    templateUrl: './add-quizz.component.html',
    styleUrls: ['./add-quizz.component.css'],
    standalone: false,
})
export class AddQuizzComponent {
    constructor(private quizzService: QuizzService) {}

    title: string = '';
    changeDetected: boolean = false;

    quizz: Quizz = {
        title: '',
        userId: 1,
        questions: [],
    };

    addQuestion() {
        let newQuestion: Question = {
            title: '',
            options: [],
        };

        for (let i = 0; i < 4; i++) {
            newQuestion.options.push({
                title: '',
                is_correct: false,
            });
        }

        this.quizz.questions.push(newQuestion);
    }

    submit() {
        this.quizz.questions.forEach((question) => {
            this.ensureFirstOptionSelected(question);
        });

        this.quizzService
            .addQuizz(quizzApi, this.quizz)
            .subscribe();
    }

    setCorrectAnswer(question: Question, optionIndex: number) {
        question.options.forEach((o) => (o.is_correct = false));

        question.options[optionIndex].is_correct = true;
    }

    ensureFirstOptionSelected(question: Question) {
        const hasCorrectOption = question.options.some((o) => o.is_correct);

        if (!hasCorrectOption && question.options.length > 0) {
            question.options[0].is_correct = true;
        }
    }

    editorCreated(editor: any) {
        console.log('Editor created:', editor);
    }

    contentChanged(event: any) {
        this.changeDetected = true;
        console.log('Content changed:', event.html);
    }

    QuillConfiguration = {
        toolbar: [
            ['bold', 'italic', 'underline'],
            [{ color: [] }, { background: [] }],
            ['link', 'image', 'video'],
            ['clean'],
        ],
    };
}
