import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { JwtPayload, SignInRequest, TokenResponse } from '../../../../types';
import { AuthService } from '../../_services/auth.service';
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
        private toastr: ToastrService
    ) { }

    signInRequest: SignInRequest = {
        username: '',
        password: '',
    };

    remember: boolean = false;

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

    onSubmit(form: any) {
        if (form.valid) {
            this.authService
                .login(this.signInRequest.username, this.signInRequest.password)
                .subscribe({
                    next: (data: TokenResponse) => {
                        this.authService.removeToken();
                        this.authService.setTokenData(data);

                        this.toastr.success(
                            'Đăng nhập thành công!',
                            'Thành công'
                        );

                        sessionStorage.setItem('userId', data.userId);

                        this.router.navigate(['home']);
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
