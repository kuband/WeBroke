import { Injectable } from '@angular/core';
import { Subject, Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { EventData } from './event.class';
import { AuthService } from '../../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class EventBusService {
  private subject$ = new Subject<EventData>();

  constructor(
    private authService: AuthService) { }

  emit(event: EventData) {
    this.subject$.next(event);
  }

  on(eventName: string, action: any): Subscription {
    return this.subject$.pipe(
      filter((e: EventData) => e.name === eventName),
      map((e: EventData) => e["value"])).subscribe(action);
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: res => {
        console.log(res);
        this.clean();
        
        window.location.reload();
      },
      error: err => {
        console.log(err);
      }
    });
  }

  ngOnInit(): void {
    this.on('logout', () => {
      this.logout();
    });
  }

  clean(): void {
    window.sessionStorage.clear();
    localStorage.clear();
  }
}