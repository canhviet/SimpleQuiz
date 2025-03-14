import { Component, Input } from '@angular/core';
import { Options, Questions, Quizz } from '../../../../../types';

@Component({
  selector: 'app-add-quizz',
  templateUrl: './add-quizz.component.html',
  styleUrls: ['./add-quizz.component.css']
})
export class AddQuizzComponent {

  quizz: Quizz = {
    title: '',
    questions: []
  };

  addQuestion() {
    let newQuestion: Questions = {
      title: '',
      options: []
    };

    for (let i = 0; i < 4; i++) {
      newQuestion.options.push({
        title: '',
        is_correct: false
      });
    }

    this.quizz.questions.push(newQuestion);

  }

  submit() {
    this.quizz.questions.forEach(question => {
        this.ensureFirstOptionSelected(question);
    });
    
    console.log(this.quizz);
  }

  setCorrectAnswer(question: Questions, optionIndex: number) {
    question.options.forEach(o => o.is_correct = false);

    question.options[optionIndex].is_correct = true;
  }

  ensureFirstOptionSelected(question: Questions) {
    const hasCorrectOption = question.options.some(o => o.is_correct);

    if (!hasCorrectOption && question.options.length > 0) {
      question.options[0].is_correct = true;
    }
  }

}