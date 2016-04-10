var createTemplateCard = function(projectJSON) {



    var content = document.getElementById('content');
    var tmpl = document.getElementById('project-card-template').content.cloneNode(true);

    tmpl.querySelector('.project-name-content').value = projectJSON.projectName; // 1
    tmpl.querySelector('.project-description').innerText = projectJSON.description; // 6
    tmpl.querySelector('.project-budget').innerText = projectJSON.budget; // 8
    tmpl.querySelector('.project-affiliatedLabs').innerText = projectJSON.affiliatedLabs;
    tmpl.querySelector('.project-updates').value = projectJSON.updates;
    tmpl.querySelector('.project-grant').innerText = projectJSON.grant;
    tmpl.querySelector('.project-dateCreated-content').innerText = projectJSON.dateCreated;
    tmpl.querySelector('.project-people-names').innerText = projectJSON.membersNames;
    tmpl.querySelector('.project-people-creator').innerText = projectJSON.creator;
    tmpl.querySelector('.project-people-lead').innerText = projectJSON.lead;


}