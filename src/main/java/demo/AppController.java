package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class AppController {

    private Person toBeDeleted;

    @Autowired
    private PersonRepository repository;

    @RequestMapping("/")
    public String index(){
        return "list";
    }

    @RequestMapping("/list")
    public String list(Model model){
        List<Person> data = new LinkedList<>();
        for (Person p: repository.findAll()){
            data.add(p);
        }
        model.addAttribute("people", data);
        return "list";
    }

    @RequestMapping("/input")
    public String input(){
        return "input";
    }

    @RequestMapping("/create")
    public String create(
            @RequestParam(name="firstname", required=true) String firstname,
            @RequestParam(name="lastname", required=true) String lastname) {
        repository.save(new Person(firstname,lastname));
        return "redirect:/list";
    }

    @RequestMapping("/show")
    public String read(
            @RequestParam(name="id", required=true) Long id,
            Model model) {
        Optional<Person> result = repository.findById(id);
        if (result.isPresent()){
            model.addAttribute("people", result.get());
            model.addAttribute("description", result.get().toString());
            return "show";
        }
        return "notfound";
    }

    @RequestMapping("/delete")
    public String delete(
            @RequestParam(name="id", required=true) Long id) {
        for (Person p: repository.findAll()){
            if (p.getId() == id){
                repository.delete(p);
                return "redirect:/list";
            }
        }
        return "notfound";
    }

    @RequestMapping("/edit")
    public String edit(
            @RequestParam(name="id", required=true) Long id,
        Model model) {
        Optional<Person> result = repository.findById(id);
        if (result.isPresent()){
            model.addAttribute("people", result.get());
            model.addAttribute("description", result.get().toString());
            toBeDeleted = result.get();
            return "edit";
        }
        return "notfound";
    }

    @RequestMapping("/update")
    public String update(
            @RequestParam(name="firstname", required=false) String firstname,
            @RequestParam(name="lastname", required=false) String lastname,
            Model model) {
        repository.delete(toBeDeleted);
        Person newPerson = new Person(firstname,lastname);
        repository.save(newPerson);
        return "redirect:/list";

    }

}