import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ForgottenPasswordComponent } from './forgotten-password/forgotten-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ListadoUsuarioComponent } from './listado-usuario/listado-usuario.component';
import { PrincipalComponent } from './principal/principal.component';
import { Component } from '@angular/core';
import { ListadoGerenteComponent } from './listado-gerente/listado-gerente.component';
import { ListadoCentroComponent } from './listado-centro/listado-centro.component';
import { ListadoMisCentrosComponent } from './listado-mis-centros/listado-mis-centros.component';
import { MisMensajesComponent } from './mis-mensajes/mis-mensajes.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'forgotten-password',
    component: ForgottenPasswordComponent
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent
  },
  {
    path: 'usuarios',
    component: ListadoUsuarioComponent
  },
  {
    path: 'gerentes',
    component: ListadoGerenteComponent
  },
  {
    path: 'centros',
    component: ListadoCentroComponent
  },
  {
    path: 'mis_centros',
    component: ListadoMisCentrosComponent
  },
  {
    path: 'mensajes',
    component: MisMensajesComponent
  },
  {
    path: '',
    component: PrincipalComponent
  }
];
