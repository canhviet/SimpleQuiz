import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule, provideAnimations } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuillModule } from 'ngx-quill';
import { RegisterComponent } from './components/register/register.component';
import { ToastrModule } from 'ngx-toastr';
import { CookieService } from 'ngx-cookie-service';
import { httpInterceptorProviders } from './_helpers/http.interceptor';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { LayoutComponent } from './components/layout/layout.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { TopbarComponent } from './components/topbar/topbar.component';
import { HomeComponent } from './components/home/home.component';
import { AddQuizzComponent } from './components/add-quizz/add-quizz.component';
import { LoginComponent } from './components/login/login.component';
import { YourQuizzComponent } from './components/your-quizz/your-quizz.component';
import { HistoryComponent } from './components/history/history.component';
import { QuizDetailComponent } from './components/quiz-detail/quiz-detail.component';
import { QuizComponent } from './components/quiz/quiz.component';

@NgModule({
    declarations: [
        AppComponent,
        AddQuizzComponent,
        RegisterComponent,
        LayoutComponent,
        SidebarComponent,
        TopbarComponent,
        HomeComponent,
        LoginComponent,
        YourQuizzComponent,
        HistoryComponent,
        QuizDetailComponent,
        QuizComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        FormsModule,
        QuillModule.forRoot(),
        ToastrModule.forRoot({
            timeOut: 3000,
            positionClass: 'toast-top-right',
            preventDuplicates: true,
        }),
    ],
    providers: [
        CookieService,
        provideAnimations(),
        httpInterceptorProviders,
        provideHttpClient(withInterceptorsFromDi()),
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}