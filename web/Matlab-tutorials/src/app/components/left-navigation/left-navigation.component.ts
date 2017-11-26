import { Component, OnInit } from '@angular/core';
import { DocumentsService } from "../../services/documents.service";
import {AuthService} from "../../services/AuthService";

@Component({
  selector: 'app-left-navigation',
  templateUrl: './left-navigation.component.html',
  styleUrls: ["./left-navigation.component.css"],
  providers: [DocumentsService, AuthService]
})
export class LeftNavigationComponent implements OnInit {

  private lessons: any[];

  constructor(private documentsService: DocumentsService,
              public authService: AuthService) { }

  ngOnInit() {
    this.documentsService.getDocuments().then((result : any[]) => {
      this.lessons = result;
    }, error => {

    });
  }

}
