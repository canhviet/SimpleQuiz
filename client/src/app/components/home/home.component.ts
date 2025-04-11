import { Component } from '@angular/core';
import { QuizzService } from '../../_services/quizz.service';
import { CookieService } from 'ngx-cookie-service';
import { JwtPayload, TokenResponse } from '../../../../types';
import { AuthService } from '../../_services/auth.service';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';

@Component({
    selector: 'app-home',
    standalone: false,
    templateUrl: './home.component.html',
    styleUrl: './home.component.css'
})
export class HomeComponent {
    constructor(private authService: AuthService, private router: Router) { }

    ngOnInit() {
        const token = this.authService.getTokenData();

        if (token) {
            const decoded = jwtDecode<JwtPayload>(token.accessToken);

            const currentTime = Math.floor(Date.now() / 1000);

            if (decoded.exp < currentTime) {
                this.authService.removeToken();
                this.router.navigate(['']);
            } else {
                this.router.navigate(['home']);
            }
        }
    }
}
