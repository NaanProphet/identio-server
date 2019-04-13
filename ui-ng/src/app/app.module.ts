import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthenticationPageComponent } from './authentication-page/authentication-page.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AlertBoxComponent } from './alert-box/alert-box.component';
import { FormsModule } from '@angular/forms';
import { LoginPasswordFormComponent } from './login-password-form/login-password-form.component';
import { U2fFormComponent } from './u2f-form/u2f-form.component';

@NgModule({
  declarations: [
    AppComponent,
    AuthenticationPageComponent,
    AlertBoxComponent,
    LoginPasswordFormComponent,
    U2fFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
