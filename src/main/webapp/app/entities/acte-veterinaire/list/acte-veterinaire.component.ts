import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IActeVeterinaire } from '../acte-veterinaire.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ActeVeterinaireService } from '../service/acte-veterinaire.service';
import { ActeVeterinaireDeleteDialogComponent } from '../delete/acte-veterinaire-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-acte-veterinaire',
  templateUrl: './acte-veterinaire.component.html',
})
export class ActeVeterinaireComponent implements OnInit {
  acteVeterinaires: IActeVeterinaire[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected acteVeterinaireService: ActeVeterinaireService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.acteVeterinaires = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.acteVeterinaireService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IActeVeterinaire[]>) => {
          this.isLoading = false;
          this.paginateActeVeterinaires(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.acteVeterinaires = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IActeVeterinaire): number {
    return item.id!;
  }

  delete(acteVeterinaire: IActeVeterinaire): void {
    const modalRef = this.modalService.open(ActeVeterinaireDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.acteVeterinaire = acteVeterinaire;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateActeVeterinaires(data: IActeVeterinaire[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.acteVeterinaires.push(d);
      }
    }
  }
}
