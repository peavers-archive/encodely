import { Routes } from '@angular/router';

export const CONTENT_ROUTES: Routes = [
  {
    path: '',
    loadChildren: () => import('../../modules/home/home.module').then((m) => m.HomeModule)
  },
  {
    path: 'settings',
    loadChildren: () => import('../../modules/settings/settings.module').then((m) => m.SettingsModule)
  }
];
