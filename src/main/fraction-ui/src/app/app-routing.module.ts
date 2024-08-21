import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PlayerListComponent} from "./features/baseball/components/player-list/player-list.component";
import {PlayerDetailComponent} from "./features/baseball/components/player-detail/player-detail.component";

const routes: Routes = [
  {path: '', component: PlayerListComponent},
  {path: 'players', component: PlayerListComponent},
  {path: 'player-detail', component: PlayerDetailComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
