import {AfterViewInit, Component, DestroyRef, inject, OnInit, ViewChild} from '@angular/core';
import {BaseballPlayerService} from "../../services/baseball-player.service";
import {map, tap, Observable} from "rxjs";
import {BaseballPlayer} from "../../interfaces/baseball-player";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {MatTableDataSource} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import { MatSort } from '@angular/material/sort';
import {
  MatBottomSheet,
  MatBottomSheetRef,
} from '@angular/material/bottom-sheet';
import { PlayerDetailComponent } from '../player-detail/player-detail.component';


@Component({
  selector: 'app-player-list',
  templateUrl: './player-list.component.html',
  styleUrls: ['./player-list.component.scss']
})
export class PlayerListComponent implements OnInit, AfterViewInit {

  destroyRef = inject(DestroyRef);
  @ViewChild(MatPaginator) paginator: MatPaginator | null = null;
  @ViewChild(MatSort) sort: MatSort | null = null;

  public displayedColumns = ['id', 'player', 'rank', 'hits', 'bats', 'year', 'ageThatYear'];
  public dataSource = new MatTableDataSource<BaseballPlayer>([]);


  constructor(private playerService: BaseballPlayerService,
              private bottomSheet: MatBottomSheet) { }

  ngOnInit(): void {
    this.retrievePlayers();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  public retrievePlayers(): void {
     this.playerService.getPlayers()
       .pipe(
         map((players: Array<BaseballPlayer>) => {
           this.dataSource.data = players;
         }),
         takeUntilDestroyed(this.destroyRef)
       ).subscribe();
  }

  public openDetail(player: BaseballPlayer): void {
    this.playerService.generatePlayerDescription(player)
      .pipe(
        map((response) => this.bottomSheet.open(PlayerDetailComponent, {data: {details: response}})),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe();
  }


}
