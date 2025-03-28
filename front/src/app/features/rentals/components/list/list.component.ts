import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { map } from 'rxjs/operators';
import { User } from 'src/app/interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { RentalsService } from '../../services/rentals.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  public rentals$ = this.rentalsService.all().pipe(
    map(response => ({
      rentals: response.rentals.map(rental => ({
        ...rental,
        picture: this.sanitizer.bypassSecurityTrustResourceUrl(rental.picture)
      }))
    }))
  );

  constructor(
    private sessionService: SessionService,
    private rentalsService: RentalsService,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {}

  get user(): User | undefined {
    return this.sessionService.user;
  }
}
