import { clearPage } from '../../utils/render';

const ObjectPage = () => {
    clearPage();
    renderObjectPage();
  };

function renderObjectPage() {
    const main = document.querySelector('main');
    const div = document.createElement('div');

    div.className='container p-5'

    div.innerHTML=`
    <section class="py-5">
      <div class="container px-4 px-lg-5 my-5">
          <div class="row gx-4 gx-lg-5 align-items-top">
              <div class="col-md-6"><img class="card-img-top mb-5 mb-md-0" src="https://dummyimage.com/600x700/dee2e6/6c757d.jpg" alt="..." /></div>
              <div class="col-md-6">
                  <div class="small mb-1">changer l'image</div>
                  <h1 class="display-12 fw-bolder">Type de l'object</h1>
                  <p class="lead">Lorem ipsum dolor sit amet consectetur adipisicing elit. Praesentium at dolorem quidem modi. Nam sequi consequatur obcaecati excepturi alias magni, accusamus eius blanditiis delectus ipsam minima ea iste laborum vero?</p>
                  <p class="lead"> Proposé par :</p>
                  <p class="lead"> Date de la proposition :</p>
                  <p class="lead"> Créneau choisi :</p>
                  <br> 

                  <p class="lead"> Etat :</p>
                  <p class="lead"> Date de changement du nouvel état :</p>
                  <p class="lead"> 
                    <div class="fs-5 mb-5">
                    Prix: <span>10.00€</span>
                    </div>
                  </p>
                  <p class="lead"> Visible :</p>
              </div>
                <center>
                  <br>
                  <a href="#" class="accept">Accepter l'objet <span class="fa fa-check"></span></a>
                  <div class="bordure_verticale"></div>
                  <textarea id="refuse" ></textarea>
                  <a href="#" class="deny">Refuser l'objet <span class="fa fa-close"></span></a>
                </center>
          </div>
      </div>
    </section>
    `
    main.appendChild(div);

}

export default ObjectPage;