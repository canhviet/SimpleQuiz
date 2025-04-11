import { Component } from '@angular/core';
import { QuizzService } from '../../_services/quizz.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../_services/auth.service';
import { Question, Quizz, TokenResponse } from '../../../../types';
import Swal from 'sweetalert2';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-quiz-detail',
  standalone: false,
  templateUrl: './quiz-detail.component.html',
  styleUrl: './quiz-detail.component.css'
})
export class QuizDetailComponent {
  constructor(
    private quizService: QuizzService,
    private toastr: ToastrService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  quiz: Quizz = {
    name: '',
    userId: Number(this.authService.getTokenData()?.userId),
    questions: []
  };

  ngOnInit() {
    if (this.authService.getTokenData() == null) {
      const sessionToken = sessionStorage.getItem('token');
      if (sessionToken) {
        const token: TokenResponse = JSON.parse(sessionToken);
        this.quiz.userId = Number(token.userId);
      } else {
        this.toastr.error("Not found user");
      }
    }

    this.route.params.subscribe((params) => {
      this.quiz.id = params['quizId'];
      this.loadQuiz(Number(this.quiz.id));
    });
  }

  loadQuiz(quizId: number) {
    this.quizService.getQuizzById(quizId).subscribe({
      next: (res) => {
        this.quiz = res.data;
      },
      error: () => {
        this.toastr.error('Failed to load quiz');
      }
    });
  }

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

    this.quiz.questions.push(newQuestion);
  }

  submit() {
    const quizRequest = {
      name: this.quiz.name,
      userId: this.quiz.userId,
      questions: this.quiz.questions.map(q => ({
        title: q.title,
        options: q.options.map(o => ({
          title: o.title,
          isCorrect: o.isCorrect
        }))
      }))
    };

    this.quizService.updateQuizz(Number(this.quiz.id), quizRequest).subscribe({
      next: () => {
        this.toastr.success('Quiz updated successfully!');
      },
      error: () => {
        this.toastr.error('Failed to update quiz');
      }
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
        this.toastr.success('Question deleted successfully!', 'Deleted');
      }
    });
  }

  removeQuestion(index: number) {
    this.quiz.questions.splice(index, 1);
  }

  hasContent(content: string): boolean {
    return content !== null && content !== undefined && content.trim() !== '' && content !== '<p><br></p>';
  }
}