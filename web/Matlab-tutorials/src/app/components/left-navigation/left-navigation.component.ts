import { Component, OnInit } from '@angular/core';
import { DocumentsService } from "../../services/documents.service";

@Component({
  selector: 'app-left-navigation',
  templateUrl: './left-navigation.component.html',
  styleUrls: ["./left-navigation.component.css"],
  providers: [DocumentsService]
})
export class LeftNavigationComponent implements OnInit {

  private lessons: any[];

  constructor(private documentsService: DocumentsService) { }

  ngOnInit() {
    this.documentsService.getDocuments().then(result => {
        this.lessons = result.json();
    })
  }

}
