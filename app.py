from flask import Flask, render_template_string, request

app = Flask(__name__)

HTML = """
<!DOCTYPE html>
<html lang="tg">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ДУГОНИК AI</title>
<style>
body {
    background: #17172b;
    color: white;
    font-family: Arial;
    text-align: center;
    padding: 25px;
}
h1 { color: #d6a5ff; }
.face { font-size: 110px; margin: 35px; }
.box {
    background: #292943;
    padding: 20px;
    border-radius: 20px;
}
input {
    padding: 15px;
    border-radius: 12px;
    border: none;
    width: 90%;
    margin-top: 15px;
}
button {
    padding: 13px 30px;
    background: #a65cff;
    color: white;
    border: none;
    border-radius: 12px;
    margin-top: 12px;
}
</style>
</head>
<body>
<h1>ДУГОНИК AI ❤️</h1>
<div class="face">😊</div>
<div class="box">
<h3>Салом! Ман дугоники ту ҳастам.</h3>
<p>Номи ман ҳоло ДУГОНИК аст.</p>
<form method="POST">
<input name="name" placeholder="Номи ту чист?" required>
<br>
<button type="submit">Фиристодан</button>
</form>
{% if name %}
<p>Хуш омадӣ, {{name}}! ❤️</p>
{% endif %}
</div>
</body>
</html>
"""

@app.route("/", methods=["GET", "POST"])
def home():
    name = request.form.get("name", "")
    return render_template_string(HTML, name=name)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
