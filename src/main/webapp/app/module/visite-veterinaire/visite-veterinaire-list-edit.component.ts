import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IVisiteVeterinaire, VisiteVeterinaire } from 'app/entities/visite-veterinaire/visite-veterinaire.model';
import { VisiteVeterinaireDeleteDialogComponent } from './visite-veterinaire-delete-dialog.component';
import { VisiteVeterinaireUpdateDialogComponent } from './visite-veterinaire-update-dialog.component';

@Component({
  selector: 'jhi-visite-veterinaire-list-edit',
  templateUrl: './visite-veterinaire-list-edit.component.html',
})
export class VisiteVeterinaireListEditComponent {
  @Input()
  visiteVeterinaires: IVisiteVeterinaire[] | null = [];

  constructor(private modalService: NgbModal) {}

  createVisiteVeterinaire(): void {
    this.openModal();
  }

  updateVisiteVeterinaire(visiteVeterinaire: VisiteVeterinaire): void {
    this.openModal(visiteVeterinaire);
  }

  deleteVisiteVeterinaire(visiteVeterinaire: VisiteVeterinaire): void {
    const ngbModal = this.modalService.open(VisiteVeterinaireDeleteDialogComponent as Component, {
      size: 'lg',
      backdrop: 'static',
    });

    ngbModal.componentInstance.visiteVeterinaire = visiteVeterinaire;
    ngbModal.result.then(
      () => {
        if (this.visiteVeterinaires) {
          const index = this.visiteVeterinaires.indexOf(visiteVeterinaire, 0);
          if (index > -1) {
            this.visiteVeterinaires.splice(index, 1);
          }
        }
      },
      () => {}
    );
  }

  private openModal(visiteVeterinaire?: VisiteVeterinaire): void {
    if (this.visiteVeterinaires) {
      let isNew = false;
      let con: VisiteVeterinaire;
      let index: number;
      if (!visiteVeterinaire) {
        isNew = true;
        con = new VisiteVeterinaire();
      } else {
        con = Object.assign({}, visiteVeterinaire);
        index = this.visiteVeterinaires.indexOf(visiteVeterinaire);
      }
      const ngbModal = this.modalService.open(VisiteVeterinaireUpdateDialogComponent as Component, {
        size: 'lg',
        backdrop: 'static',
      });
      ngbModal.componentInstance.visiteVeterinaire = con;
      ngbModal.result.then(
        c => {
          if (this.visiteVeterinaires) {
            if (isNew) {
              this.visiteVeterinaires.push(c);
            } else {
              this.visiteVeterinaires[index] = c;
            }
          }
        },
        () => {}
      );
    }
  }
}
