import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IActeVeterinaire, ActeVeterinaire } from 'app/entities/acte-veterinaire/acte-veterinaire.model';
import { ActeVeterinaireDeleteDialogComponent } from './acte-veterinaire-delete-dialog.component';
import { ActeVeterinaireUpdateDialogComponent } from './acte-veterinaire-update-dialog.component';

@Component({
  selector: 'jhi-acte-veterinaire-list-edit',
  templateUrl: './acte-veterinaire-list-edit.component.html',
})
export class ActeVeterinaireListEditComponent {
  @Input()
  acteVeterinaires: IActeVeterinaire[] | null = [];

  constructor(private modalService: NgbModal) {}

  createActeVeterinaire(): void {
    this.openModal();
  }

  updateActeVeterinaire(acteVeterinaire: ActeVeterinaire): void {
    this.openModal(acteVeterinaire);
  }

  deleteActeVeterinaire(acteVeterinaire: ActeVeterinaire): void {
    const ngbModal = this.modalService.open(ActeVeterinaireDeleteDialogComponent as Component, {
      size: 'lg',
      backdrop: 'static',
    });

    ngbModal.componentInstance.acteVeterinaire = acteVeterinaire;
    ngbModal.result.then(
      () => {
        if (this.acteVeterinaires) {
          const index = this.acteVeterinaires.indexOf(acteVeterinaire, 0);
          if (index > -1) {
            this.acteVeterinaires.splice(index, 1);
          }
        }
      },
      () => {}
    );
  }

  private openModal(acteVeterinaire?: ActeVeterinaire): void {
    if (this.acteVeterinaires) {
      let isNew = false;
      let con: ActeVeterinaire;
      let index: number;
      if (!acteVeterinaire) {
        isNew = true;
        con = new ActeVeterinaire();
      } else {
        con = Object.assign({}, acteVeterinaire);
        index = this.acteVeterinaires.indexOf(acteVeterinaire);
      }
      const ngbModal = this.modalService.open(ActeVeterinaireUpdateDialogComponent as Component, {
        size: 'lg',
        backdrop: 'static',
      });
      ngbModal.componentInstance.acteVeterinaire = con;
      ngbModal.result.then(
        c => {
          if (this.acteVeterinaires) {
            if (isNew) {
              this.acteVeterinaires.push(c);
            } else {
              this.acteVeterinaires[index] = c;
            }
          }
        },
        () => {}
      );
    }
  }
}
