import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { LayoutComponent } from './components/layout/layout.component';
import { AddQuizzComponent } from './components/add-quizz/add-quizz.component';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './auth.guard';
import { YourQuizzComponent } from './components/your-quizz/your-quizz.component';
import { HistoryComponent } from './components/history/history.component';
import { QuizComponent } from './components/quiz/quiz.component';
import { QuizDetailComponent } from './components/quiz-detail/quiz-detail.component';

const routes: Routes = [
    {
        path: '',
        component: LoginComponent,
    },
    {
        path: 'login',
        component: LoginComponent,
    },
    {
        path: 'sign_up',
        component: RegisterComponent,
    },
    {
        path: 'home',
        component: LayoutComponent,
        canActivate: [AuthGuard],
        children: [
            {
                path: '',
                component: HomeComponent,
            },
            {
                path: 'add_quiz',
                component: AddQuizzComponent,
            },
            {
                path: 'your_quiz',
                component: YourQuizzComponent
            },
            {
                path: 'history',
                component: HistoryComponent
            },
            {
                path: 'quiz-detail/:quizId',
                component: QuizDetailComponent
            }
        ],
    },

    {
        path: 'quiz/:quizId',
        component: QuizComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule { }
