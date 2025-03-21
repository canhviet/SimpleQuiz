import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SignInRequest } from '../../../../types';
import { AuthService } from '../../_services/auth.service';
import { CookieService } from 'ngx-cookie-service';
import { ToastrService } from 'ngx-toastr';
import { jwtDecode } from 'jwt-decode';

@Component({
    selector: 'app-login',
    standalone: false,
    templateUrl: './login.component.html',
    styleUrl: './login.component.css',
})
export class LoginComponent {
    constructor(
        private router: Router,
        private authService: AuthService,
        private cookieService: CookieService,
        private toastr: ToastrService
    ) {}

    signInRequest: SignInRequest = {
        username: '',
        password: '',
    };

    remember: boolean = false;

    ngOnInit() {
        const token = this.cookieService.get('authToken');

        if (token) {
            this.router.navigate(['home']);
            const decoded = jwtDecode(token);
            console.log('Thông tin token:', decoded);
        }
    }

    onSubmit() {
        if (true) {
            this.authService
                .login(this.signInRequest.username, this.signInRequest.password)
                .subscribe({
                    next: (data) => {
                        if (this.remember == true) {
                            this.cookieService.set(
                                'authToken',
                                data.accessToken
                            );
                        } else {
                            window.sessionStorage.setItem(
                                'accessToken',
                                data.accessToken
                            );
                        }

                        this.toastr.success(
                            'Đăng nhập thành công!',
                            'Thành công'
                        );
                    },
                    error: (error) => {
                        this.toastr.error('Đăng nhập không thành công!', error);
                    },
                });
        }
    }

    redirectRegister() {
        this.router.navigate(['register']);
    }
}
