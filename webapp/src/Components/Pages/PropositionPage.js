
import { clearPage } from '../../utils/render';

const PropositionPage = () => {
    clearPage();
    renderPropositionPage();
  };

function renderPropositionPage() {
    const main = document.querySelector('main');
    const form = document.createElement('div');

    form.className='container p-5'

    form.innerHTML=`
    <h1 class="text-center">Proposez un objet</h1>
    <div class="row justify-content-center">
        <h5 class="text-center col-10">Vous avez un objet encore en bon état à proposer ? Remplissez le formulaire ci-dessous. Si la proposition est acceptée, vous pourrez venir déposer l’objet au parc à conteneurs à la date et plage horaire que vous aurez choisi.</h3>

        <form class="col-12 col-xl-8">
            <div class="mb-3">
                <div class="row row-cols-1 row-cols-md-2">
                    <div class="col mb-3">

                        <br>

                        <h4>Date et plage horaire</h4>

                        <br>

                        <label for="input-dateEtPlage" class="form-label">04/02/2023</label>
                        <input type="radio" class="btn-check" name="options-outlined" id="success-outlined" autocomplete="off" >
                        <label class="btn btn-outline-secondary" for="success-outlined">11h à 13h</label>
                        
                        <input type="radio" class="btn-check" name="options-outlined" id="danger-outlined" autocomplete="off">
                        <label class="btn btn-outline-secondary" for="danger-outlined">14h à 16h</label>

                        <br>

                        <label for="input-dateEtPlage1" class="form-label">11/02/2023</label>
                        <input type="radio" class="btn-check" name="options-outlined" id="success-outlined1" autocomplete="off" >
                        <label class="btn btn-outline-secondary" for="success-outlined1">11h à 13h</label>
                        
                        <input type="radio" class="btn-check" name="options-outlined" id="danger-outlined1" autocomplete="off">
                        <label class="btn btn-outline-secondary" for="danger-outlined1">14h à 16h</label>

                        <br>

                        <label for="input-dateEtPlage1" class="form-label">18/02/2023</label>
                        <input type="radio" class="btn-check" name="options-outlined" id="success-outlined2" autocomplete="off" >
                        <label class="btn btn-outline-secondary" for="success-outlined2">11h à 13h</label>
                        
                        <input type="radio" class="btn-check" name="options-outlined" id="danger-outlined2" autocomplete="off">
                        <label class="btn btn-outline-secondary" for="danger-outlined2">14h à 16h</label>

                    </div>

                    <div class="col mb-3">
                        <br>
                        <h4>Type d'objet</h4>

                        <div class="form-group">
                            <select class="form-select" aria-label="Default select example">
                                <option selected>Open this select menu</option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>

                        <br>

                        <h4>Photo de l'objet</h4>

                    <div class="form-group">
                        <div class="input-group">
                            <input type="text" id="fileName" class="form-control" readonly ng-model="fileName" ng-click="browse()">
                            <span class="input-group-btn">
                            <button type="button" class="btn btn-secondary" ng-click="browse()">Browse</button>
                            </span>
                        </div>                
                        <label for="fileName">Select a file</label>
                    </div>

                </div>

            </div>

            <div class="form-group">
                <label for="exampleFormControlTextarea1">Description de l'objet(max. 120 caractères)</label>
                <textarea class="form-control" id="exampleFormControlTextarea1" ></textarea>
            </div>

            <div class="form-group text-center">
            <br>
            <button type="button" class="btn btn-secondary btn-lg btn-block">S'inscrire</button>
            
            <button type="button" class="btn btn-secondary btn-lg btn-block">Se connecter</button>
            </div>

            <br>

            <p class="text-center">ou</p>

            <div class="form-group">
                <div class="input-group w-auto">
                <input type="text" class="form-control" placeholder="Numéro de téléphone"/>
                <button class="btn btn-secondary" type="button" id="button-addon1" data-mdb-ripple-color="dark">
                    Soumettre anonymement
                </button>
                </div>
            </div>

        </div>
            

        </form>
    
    `
    main.appendChild(form);

}

export default PropositionPage;