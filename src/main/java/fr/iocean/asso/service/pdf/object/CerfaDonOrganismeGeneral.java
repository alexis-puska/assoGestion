package fr.iocean.asso.service.pdf.object;

import fr.iocean.asso.service.pdf.annotation.PdfField;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CerfaDonOrganismeGeneral implements Serializable {

    private static final long serialVersionUID = -7120441970774515997L;

    public static final String PDF_FIELD_OFF = "Off";
    public static final String PDF_FIELD_ON = "On";

    @PdfField(name = "Numéro dordre du reçu")
    private String numeroOrdreRecu;

    @PdfField(name = "Adresse")
    private String adresse;

    @PdfField(name = "N")
    private String numero;

    @PdfField(name = "Rue")
    private String rue;

    @PdfField(name = "Code Postal")
    private String codePostal;

    @PdfField(name = "Commune")
    private String commune;

    @PdfField(name = "Objet 1")
    private String objet1;

    @PdfField(name = "Objet 2")
    private String objet2;

    @PdfField(name = "Objet 3")
    private String objet3;

    @PdfField(name = "Cochez la case concernée 1")
    private String cochezLaCaseConcernee1;

    @PdfField(name = "date1")
    private String date1;

    @PdfField(name = "Association ou fondation reconnue dutilité publique par décret en date du")
    private String associationOuFondationReconnueDutilitePubliqueParDecretEnDateDu = PDF_FIELD_OFF;

    @PdfField(name = "Fondation universitaire ou fondation partenariale mentionnées respectivement aux articles L 71912 et")
    private String fondationUniversitaireOuFondationPartenarialeMentionnéesRespectivementAuxArticlesL71912Et = PDF_FIELD_OFF;

    @PdfField(name = "Fondation dentreprise")
    private String fondationDentreprise = PDF_FIELD_OFF;

    @PdfField(name = "Oeuvre ou organisme dintérêt général")
    private String oeuvreOuOrganismeDinteretGeneral = PDF_FIELD_OFF;

    @PdfField(name = "Musée de France")
    private String museeDeFrance = PDF_FIELD_OFF;

    @PdfField(name = "Etablissement denseignement supérieur ou denseignement artistique public ou privé dintérêt général à")
    private String etablissementDenseignementSuperieurOuDenseignementArtistiquePublicOuPriveDinteretGeneralA = PDF_FIELD_OFF;

    @PdfField(name = "Organisme ayant pour objectif exclusif de participer financièrement à la création dentreprises")
    private String organismeAyantPourObjectifExclusifDeParticiperFinancierementALaCreationDentreprises = PDF_FIELD_OFF;

    @PdfField(name = "Association cultuelle ou de bienfaisance et établissement public reconnus dAlsaceMoselle")
    private String associationCultuelleOuDeBienfaisanceEtEtablissementPublicReconnusDAlsaceMoselle = PDF_FIELD_OFF;

    @PdfField(name = "Organisme ayant pour activité principale lorganisation de festivals")
    private String organismeAyantPourActivitePrincipaleLorganisationDeFestivals = PDF_FIELD_OFF;

    @PdfField(name = "Association fournissant gratuitement une aide alimentaire ou des soins médicaux à des personnes en")
    private String associationFournissantGratuitementUneAideAlimentaireOuDesSoinsMedicauxADesPersonnesEn = PDF_FIELD_OFF;

    @PdfField(name = "Fondation du patrimoine ou fondation ou association qui affecte irrévocablement les dons à la Fondation du")
    private String fondationDuPatrimoineOuFondationOuAssociationQuiAffecteIrrevocablementLesDonsALaFondationDu = PDF_FIELD_OFF;

    @PdfField(name = "Etablissement de recherche public ou privé dintérêt général à but non lucratif")
    private String etablissementDeRecherchePublicOuPriveDinteretGeneralAButNonLucratif = PDF_FIELD_OFF;

    @PdfField(name = "Entreprise dinsertion ou entreprise de travail temporaire dinsertion articles L 51325 et L 51326 du")
    private String EntrepriseDinsertionOuEntrepriseDeTravailTemporaireDinsertionArticlesL51325EtL51326Du = PDF_FIELD_OFF;

    @PdfField(name = "Association intermédiaire article L51327 du code du travail")
    private String AssociationIntermediaireArticleL51327DuCodeDuTravail = PDF_FIELD_OFF;

    @PdfField(name = "Ateliers et chantiers dinsertion article L513215 du code du travail")
    private String AteliersEtChantiersDinsertionArticleL513215DuCodeDuTravail = PDF_FIELD_OFF;

    @PdfField(name = "Entreprises adaptées article L521313 du code du travail")
    private String EntreprisesAdapteesArticleL521313DuCodeDuTravail = PDF_FIELD_OFF;

    @PdfField(name = "Agence nationale de la recherche ANR")
    private String AgenceNationaleDeLaRechercheAnr = PDF_FIELD_OFF;

    @PdfField(name = "Société ou organisme agrée de recherche scientifique ou technique 2")
    private String SocieteOuOrganismeAgreeDeRechercheScientifiqueOuTechnique2 = PDF_FIELD_OFF;

    @PdfField(name = "Autres organisme")
    private String autresOrganisme = PDF_FIELD_OFF;

    @PdfField(name = "date2")
    private String date2;

    @PdfField(name = "date3")
    private String date3;

    @PdfField(name = "undefined")
    private String undefined;

    @PdfField(name = "Nom")
    private String nom;

    @PdfField(name = "Prénoms")
    private String prenom;

    @PdfField(name = "Adresse_2")
    private String adresse2;

    @PdfField(name = "Code Postal_2")
    private String codePostal2;

    @PdfField(name = "Commune_2")
    private String commune2;

    @PdfField(name = "Euros")
    private String euros;

    @PdfField(name = "Somme en toutes lettres")
    private String sommeEnToutesLettres;

    @PdfField(name = "date4")
    private String date4;

    @PdfField(name = "200 du CGI")
    private String duCGI200 = PDF_FIELD_OFF;

    @PdfField(name = "238 bis du CGI")
    private String duCGI238Bis = PDF_FIELD_OFF;

    @PdfField(name = "978 du CGI")
    private String duCGI978 = PDF_FIELD_OFF;

    @PdfField(name = "réduction dimpôt prévue à larticle 3")
    private String reductionImpotPrevueArticle3;

    @PdfField(name = "Acte authentique")
    private String acteAuthentique = PDF_FIELD_OFF;

    @PdfField(name = "Acte sous seing privé")
    private String acteSousSeingPrive = PDF_FIELD_OFF;

    @PdfField(name = "Déclaration de don manuel")
    private String declarationDeDonManuel = PDF_FIELD_OFF;

    @PdfField(name = "Autres")
    private String autres = PDF_FIELD_OFF;

    @PdfField(name = "Nature du don")
    private String natureDuDon;

    @PdfField(name = "Numéraire")
    private String numeraire = PDF_FIELD_OFF;

    @PdfField(name = "Titres de sociétés cotés")
    private String titresDeSocietesCotes = PDF_FIELD_OFF;

    @PdfField(name = "Autres 4")
    private String autres4 = PDF_FIELD_OFF;

    @PdfField(name = "En cas de don en numéraire mode de versement du don")
    private String enCasDeDonEnNumeraireModeDeVersementDuDon;

    @PdfField(name = "Remise despèces")
    private String remiseEspece = PDF_FIELD_OFF;

    @PdfField(name = "Chèque")
    private String cheque = PDF_FIELD_OFF;

    @PdfField(name = "Virement prélèvement carte bancaire")
    private String virementPrelevementCarteBancaire = PDF_FIELD_OFF;

    @PdfField(name = "date5")
    private String date5;

    @PdfField(name = "fill_11")
    private String fill11;
}
