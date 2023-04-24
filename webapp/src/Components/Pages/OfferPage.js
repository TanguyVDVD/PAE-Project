import flatpickr from 'flatpickr';
import "flatpickr/dist/l10n/fr";
import {clearPage, renderError} from '../../utils/render';
import API from "../../utils/api";
import {
    getTodaySDate,
    invertDateFormat,
    subtractDates
} from "../../utils/dates";
import {getAuthenticatedUser} from "../../utils/auths";
import Navigate from "../Router/Navigate";

const OfferPage = () => {
    clearPage();

    API.get('/objectTypes')
    .then((objectTypes) => {
        renderOfferPage(objectTypes);
    }).catch((err) => {
        renderError(err.message);
    });
};

function renderOfferPage(objectTypes) {
    let isSubmitting = false;

    const authenticatedUser = getAuthenticatedUser();

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
                        
                        <input type="radio" class="btn-check" name="options-outlined" id="input-morning" autocomplete="off" >
                        <label class="btn btn-outline-primary text-secondary" for="input-morning">11h à 13h</label>
                        
                        <input type="radio" class="btn-check" name="options-outlined" id="input-afternoon" autocomplete="off">
                        <label class="btn btn-outline-primary text-secondary" for="input-afternoon">14h à 16h</label>

                        <br>

                        <div id="div-date-picker" class="mt-4">
                          <input type="text" id="input-receipt-date">
                        </div>
                        
                    </div>

                    <div class="col mb-3">
                        <br>
                        <h4>Type d'objet</h4>

                        <div class="form-group">
                            <select class="form-select" type="text" aria-label="Default select example" id="input-objectType">
                                ${objectTypes.map(
                                    (objectType) =>
                                        `
                                            <option>${objectType.label}</option>
                                        `,
                                    )}
                            </select>
                        </div>

                        <br>

                        <h4>Photo de l'objet</h4>

                        <div class="col mb-3">
                            <input
                              type="file"
                              accept="image/png, image/jpeg"
                              class="form-control"
                              id="input-photo"
                              placeholder=""
                            />
                        </div>
                    </div>

                </div>

                <div class="form-group">
                    <label for="exampleFormControlTextarea1">Description de l'objet (max. 120 caractères)</label>
                    <textarea class="form-control" id="input-description" type="text"></textarea>
                </div>
                
                ${authenticatedUser ? 
                    `
                        <div class="form-group text-center">
                            <br>
                            <button type="submit" class="btn btn-primary btn-lg btn-block text-secondary" id="submit-btn">Soumettre</button>
                        </div>
                    ` : 
                    `
                        <div class="form-group text-center">
                            <br>
                            <button type="button" class="btn btn-primary btn-lg btn-block text-secondary" id="register">S'inscrire</button>
                            <button type="button" class="btn btn-primary btn-lg btn-block text-secondary" id="login">Se connecter</button>
                        </div>
        
                        <br>
        
                        <p class="text-center">ou</p>
        
                        <div class="form-group">
                            <div class="input-group w-auto">
                                <input type="text" class="form-control" placeholder="Numéro de téléphone" id="input-phone-number"/>
                                <button class="btn btn-primary text-secondary" type="submit" id="submit-btn-anonymous" data-mdb-ripple-color="dark">Soumettre anonymement</button>
                            </div>
                        </div> 
                    `
                }
                
            </div>
        </form>
    
    `
    main.appendChild(form);

    const enableDates = [];

    API.get('/availabilities').then((availabilities) => {
        availabilities.forEach((item) => {
            enableDates.push(invertDateFormat(item.date));
        });
        renderDatePicker("#input-receipt-date",enableDates);
    }).catch((err) => {
        renderError(err.message);
    });

    if (!authenticatedUser){
        document.getElementById("register").addEventListener("click", () => {
            Navigate('/register');
        });

        document.getElementById("login").addEventListener("click", () => {
            Navigate('/login');
        });
    }

    form.querySelector('form').addEventListener('submit', (e) => {
        e.preventDefault();

        if (isSubmitting) return;
        isSubmitting = true;

        renderError();

        const formData = new FormData();

        if(!authenticatedUser){
            formData.append("phoneNumber", document.getElementById("input-phone-number").value);
        }

        if(document.getElementById("input-morning").checked){
            formData.append("timeSlot", "matin");
        } else if(document.getElementById("input-afternoon").checked){
            formData.append("timeSlot", "après-midi");
        }

        ['description', 'objectType'].forEach((key) => {
            const input = form.querySelector(`#input-${key}`);

            formData.append(key, input.value);
        });

        const file = document.getElementById("input-photo").files[0];

        if (file){
            formData.append("photo", file);
        }

        const date = document.getElementById("input-receipt-date").value;

        if (date){
            formData.append("receiptDate", invertDateFormat(date));
        }

        API.post('objects', { body: formData })
        .then(() => {
            Navigate('/');
        })
        .catch((err) => {
            renderError(err.message);
        })
        .finally(() => {
            isSubmitting = false;
        });
    });
}

function renderDatePicker(datePickerId, availabilities) {
    if (isThereEnableDate(availabilities)){
        flatpickr(datePickerId, {
            locale: "fr",
            dateFormat: "d-m-Y",
            minDate: "today",
            enable: availabilities,
        });
    } else {
        document.getElementById("div-date-picker").innerHTML = `
            <p class="text-danger">Aucune date disponible : Réessayez ultérieurement</p>
        `;
    }

}

function isThereEnableDate(dates){
    const today = new Date(getTodaySDate());
    let bool = false;

    dates.forEach((date) => {
        if (subtractDates(today, new Date(invertDateFormat(date))) > 0){
            bool = true;
        }
    })

    return bool;
}

export default OfferPage;