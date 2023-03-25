import {clearPage} from '../../utils/render';
import { getAuthenticatedUser } from '../../utils/auths';
import API from "../../utils/api";


const ObjectPage = (params) => {
  const id = parseInt(params.id, 10);

  clearPage();

  API.get(`object/${id}`)
  .then((object) => {
    renderObjectPage(object);
  })

  renderObjectPage(object);
};

function renderObjectPage(object) {
    const main = document.querySelector('main');

    const authenticatedUser = getAuthenticatedUser();

    const div = document.createElement('div');

    div.className='container p-5'

    div.innerHTML=`
    <section class="py-5">
      <div class="container px-4 px-lg-5 my-5">
          <div class="row gx-4 gx-lg-5 align-items-top">
              <div class="col-md-6">
                <img class="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg" alt="..." />
              </div>

              <div class="col-md-6">
              ${
                authenticatedUser && authenticatedUser.helper
                  ? `
                <form action="">
                    <div class="small mb-1">changer l'image
                      <input type="button" value="parcourir" onclick="" display="inline-block" />
                    </div>
                    `
                    : ''
              }

                    <h1 class="display-12 fw-bolder">${object.objectType}

                  ${
                      authenticatedUser && authenticatedUser.helper
                      ? `
                          <a href="" id="modifyButtonType">
                            <img src="https://cdn-icons-png.flaticon.com/512/1014/1014883.png " alt="" width="20px">
                          </a> 
                          `
                          : ''
                        }

                    </h1>

                    <p class="lead">
                      Lorem ipsum dolor sit amet consectetur adipisicing elit. Praesentium at dolorem quidem modi. Nam sequi consequatur obcaecati excepturi alias magni, accusamus eius blanditiis delectus ipsam minima ea iste laborum vero?
                       
                      ${
                        authenticatedUser && authenticatedUser.helper
                          ? `
                        <a href="" id="modifyButtonDescription">
                          <img src="https://cdn-icons-png.flaticon.com/512/1014/1014883.png " alt="" width="10px">
                        </a>
                        `
                        : ''
                      }
                      
                    </p>
                    ${
                      authenticatedUser && authenticatedUser.helper
                        ? `

                    <p class="lead"> Proposé par :</p>
                    <p class="lead"> Date de la proposition :</p>
                    <p class="lead"> Créneau choisi :</p>
                    <br> 

                    <p class="lead"> Etat :
                      <select name="etats" id="etat">
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                      </select>
                    </p>

                    <p class="lead"> Date de changement du nouvel état :
                        <input type="text" id="date_prop" placeholder="date" />
                    </p>
                    `
                    : ''
                  }

                  ${
                    authenticatedUser && authenticatedUser.helper
                      ? `
                      <p class="lead"> 
                      <input type="text" id="prix" placeholder="nouveau prix" />
                      </p>
                      `
                      : ''
                    }

                    <p class="lead"> 
                        Prix: <span>10.00€</span>
                    </p>
                    
                    ${
                      authenticatedUser && authenticatedUser.helper
                        ? `
                    <p class="lead"> Visible :    
                      <input type="radio" id="oui" name="oui" value="oui">
                      <label for="oui">oui</label>

                      <input type="radio" id="non" name="non" value="non">
                      <label for="non">non</label>
                    </p>

                    <br> 

                    <button type="button" class="btn btn-secondary btn-lg btn-block">Sauvegarder</button>
                  </form>
                </div>
                    
                <center>
                  <br>
                  <a href="#" class="accept">Accepter l'objet <span class="fa fa-check"></span></a>

                  <div class="bordure_verticale"></div>

                  <textarea id="refuse"></textarea>
                  <a href="#" class="deny">Refuser l'objet <span class="fa fa-close"></span></a>
                </center>
                `
                : ''
              }
            </div>
        </div>
    </section>
    `

  


    main.appendChild(div);

}

export default ObjectPage;