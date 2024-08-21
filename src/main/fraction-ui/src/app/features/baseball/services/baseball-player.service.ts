import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {BaseballPlayer} from "../interfaces/baseball-player";
import {Page} from "../interfaces/page";

class Void {
}

@Injectable({
  providedIn: 'root'
})
export class BaseballPlayerService {

  private readonly api = 'http://localhost:8080/api';
  private readonly endpoint = 'baseball';
  private readonly baseUrl = `${this.api}/${this.endpoint}`;

  private readonly playersUrl = `${this.baseUrl}/players`;
  private readonly playerUrl = `${this.baseUrl}/player`;

  private readonly playerByIdUrl = `${this.playerUrl}/:id`; 
  private readonly playerDescriptionUrl = `${this.playerUrl}/description`


  constructor(private httpClient: HttpClient) { }

  public getPlayers(): Observable<Array<BaseballPlayer>> {
    return this.httpClient.get<Array<BaseballPlayer>>(this.playersUrl);
  }

  public getPlayersPaged(page: number, size: number, sortBy: string, sortDirection: 'ASC' | 'DESC'): Observable<Page<BaseballPlayer>> {
    const httpParams = new HttpParams().set('page', page).set('size', size).set('sortBy', sortBy).set('sortDirection', sortDirection);
    return this.httpClient.get<Page<BaseballPlayer>>(this.playersUrl, {params: httpParams});
  }

  public getPlayer(id: number): Observable<BaseballPlayer> {
    return this.httpClient.get<BaseballPlayer>(this.playerByIdUrl.replace(':id', String(id)));
  }

  public createPlayer(player: BaseballPlayer): Observable<BaseballPlayer>{
    return this.httpClient.post<BaseballPlayer>(this.playerUrl, player);
  }

  public updatePlayer(player: BaseballPlayer): Observable<BaseballPlayer>{
    return this.httpClient.put<BaseballPlayer>(this.playerUrl, player);
  }

  public deletePlayer(id: number): Observable<Void> {
    return this.httpClient.get<BaseballPlayer>(this.playerByIdUrl.replace(':id', String(id)));
  }
  
  public generatePlayerDescription(player: BaseballPlayer): Observable<BaseballPlayer> {
    return this.httpClient.post<BaseballPlayer>(this.playerDescriptionUrl, player);
  }
}
